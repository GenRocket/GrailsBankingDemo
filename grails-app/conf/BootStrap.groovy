class BootStrap {
    def bootstrapService

    def init = { servletContext ->

        environments {
            development {
                bootstrapService.createBranch()
                bootstrapService.createCard()
            }
        }
    }
    def destroy = {
    }
}
