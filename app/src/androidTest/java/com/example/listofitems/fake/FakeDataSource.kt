package com.example.listofitems.fake

import com.example.listofitems.model.Item


object FakeDataSource {
    val itemList = listOf(
        Item( listId="1", name= "Item 684"),
        Item( listId="3" , name= "Item 276"),
        Item( listId="4" , name= "Item 808"),
        Item( listId="5" , name= "Item 908"),
        Item( listId="2" , name= "Item 735"),
        Item( listId="7" , name= "Item 345"),
        Item( listId="6" , name= "Item 123"),
        Item( listId="8" , name= "Item 789")
    ).sortedBy { it.listId }
}