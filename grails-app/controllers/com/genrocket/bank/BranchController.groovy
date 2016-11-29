package com.genrocket.bank

class BranchController {
  def branchService

  def list() {
    params.max = Math.min(params.max ? params.max.toInteger() : 15, 100)
    params.sort = "id"
    params.order = "desc"
    List<Branch> branches = Branch.list(params)
    [branches: branches, count: Branch.count()]
  }

  def edit(Long id) {
    [branch: id ? Branch.get(id) : null]
  }

  def save(Branch branch) {
    if (branch.validate()) {
      branchService.save(branch)
      flash.message = message(message: "branch.successfully.edited")
      redirect(action: 'list')
    } else {
      render(view: 'edit', model: [branch: branch])
    }
  }
}
