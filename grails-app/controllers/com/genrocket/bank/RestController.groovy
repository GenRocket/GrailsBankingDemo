package com.genrocket.bank

import grails.converters.JSON

class RestController {
  def branchService

  def createBranch() {
    Map branchMap = request.JSON as Map
    Branch branch = new Branch()
    branch.properties = branchMap.branch
    branchService.save(branch)
    render ([success: true] as JSON)
  }
}
