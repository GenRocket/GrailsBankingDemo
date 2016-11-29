package com.genrocket.bank

class BranchController {

  def list() {
    params.max = Math.min(params.max ? params.max.toInteger() : 15, 100)
    params.sort = "id"
    params.order = "desc"
    List<Branch> branches = Branch.list(params)
    [branches: branches, count: Branch.count()]
  }
}
