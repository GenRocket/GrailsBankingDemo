package com.genrocket.bank

class UserController {
  def userService
  def customerService

  def list() {
    params.max = Math.min(params.max ? params.max.toInteger() : 15, 100)
    params.sort = "id"
    params.order = "desc"
    List<User> users = User.list(params)
    [users: users, count: User.count()]
  }

  def edit(Long id) {
    [user: id ? User.get(id) : new User()]
  }

  def save(User user) {
    boolean isNewUser = !user.id
    if (user.validate()) {
      userService.save(user)
      flash.message = isNewUser ? message(message: "user.successfully.added") : message(message: "user.successfully.edited")
      redirect(action: 'list')
    } else {
      render(view: 'edit', model: [user: user])
    }
  }

  def accounts(Long id) {
    User user = id ? User.get(id) : null
    List<Customer> customers = user ? Customer.findAllByUser(user) : []
    [customers: customers, user: user]
  }

  def enable(Long id) {
    Customer customer = id ? Customer.get(id) : null
    if (customer) {
      customerService.enableCustomer(customer)
      redirect(action: 'accounts', params: [id: customer.user.id])
    } else {
      redirect(action: 'list')
    }
  }

  def disable(Long id) {
    Customer customer = id ? Customer.get(id) : null
    if (customer) {
      customerService.disableCustomer(customer)
      redirect(action: 'accounts', params: [id: customer.user.id])
    } else {
      redirect(action: 'list')
    }
  }
}
