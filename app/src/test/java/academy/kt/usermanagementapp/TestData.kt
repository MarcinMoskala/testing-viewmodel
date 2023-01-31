package academy.kt.usermanagementapp

import academy.kt.usermanagementapp.model.User

object TestData {
    val user1 = User(1, "Alex", "some@gmail.com", "active")
    val user2 = User(2, "Jake", "jake@gma.com", "inactive")
    val user3 = User(3, "Franc", "fff@o2.pl", "active")
    val users1 = listOf(user1, user2)
    val users2 = listOf(user1, user3)
}