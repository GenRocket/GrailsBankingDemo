package com.genrocket.bank

class UserController {
  def userService

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
}
