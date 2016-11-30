package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class UserControllerIntegrationSpec extends IntegrationSpec {

    def userTestDataService
    def messageSource

    void "test User list"() {
        given:
        userTestDataService.loadData(20)

        when:
        UserController controller = new UserController()
        Map map = controller.list()

        then:
        map.count == User.count()
        map.users == User.list([max: 15, sort: 'id', order: 'desc'])
    }

    void "test User list with pagination"() {
        given:
        userTestDataService.loadData(10)

        when:
        UserController controller = new UserController()
        controller.params.max = 5
        controller.params.offset = 5
        Map map = controller.list()

        then:
        map.count == User.count()
        map.users == User.list([max: 5, offset: 5, sort: 'id', order: 'desc'])

    }

    void "test User edit"() {
        given:
        userTestDataService.loadData()
        User user = User.first()

        when:
        UserController controller = new UserController()
        Map map = controller.edit(user.id)

        then:
        map.user == user
        map.user.id
    }

    void "test User add"() {
        given:

        when:
        UserController controller = new UserController()
        Map map = controller.edit(null)

        then:
        map.user != null
        !map.user.id
    }

    void "test User save for newly added"() {
        given:
        List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
        User user = (User) userList.first().object

        when:
        UserController controller = new UserController()
        controller.save(user)

        then:
        user.id
        User.count()
        controller.flash.message == messageSource.getMessage("user.successfully.added", null, null)
        controller.response.redirectedUrl == '/user/list'
    }

    void "test User save for edited"() {
        given:
        List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
        User user = (User) userList.first().object
        user.save(flush: true)

        when:
        UserController controller = new UserController()
        controller.save(user)

        then:
        user.id
        User.count()
        controller.flash.message == messageSource.getMessage("user.successfully.edited", null, null)
        controller.response.redirectedUrl == '/user/list'
    }

    void "test validation error on User save"() {
        given:
        List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
        User user = (User) userList.first().object
        user.firstName = null

        when:
        UserController controller = new UserController()
        controller.save(user)

        then:
        !user.id
        !User.count()
        user.errors.getFieldError("firstName").code == "nullable"
        controller.modelAndView.viewName == '/user/edit'
    }
}
