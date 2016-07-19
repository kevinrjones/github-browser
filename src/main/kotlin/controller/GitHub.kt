package controller

import javafx.beans.property.SimpleObjectProperty
import model.Issue
import model.Repo
import model.User
import tornadofx.*

class GitHub : Controller() {
    val selectedUserProperty = SimpleObjectProperty<User>()
    var selectedUser by selectedUserProperty

    val selectedRepoProperty = SimpleObjectProperty<Repo>()
    var selectedRepo by selectedRepoProperty

    private val api: Rest by inject()

    init {
        api.baseURI = "https://api.github.com/"
    }

    fun listIssues(username: String = "edvin", repo: String = "tornadofx", state: Issue.State)
            = api.get("repos/$username/$repo/issues?state=$state").list().toModel<Issue>()

    fun listRepos(username: String = selectedUser.login)
            = api.get("users/$username/repos").list().toModel<Repo>()

    fun login(username: String, password: String): Boolean {
        api.setBasicAuth(username, password)
        val result = api.get("user")
        val json = result.one()
        if (result.ok()) {
            selectedUser = json.toModel()
            return true
        }
        return false
    }
}
