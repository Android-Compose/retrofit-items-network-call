package com.example.listofitems.utils

import com.example.listofitems.model.Item
import java.util.SortedMap



fun List<Item>.processSortedItems(): SortedMap<Int, List<Item>>{
    return this.filter { !it.name.isNullOrBlank() }
        .sortedBy { it.name?.substringAfter(" ")?.toInt() } // Sort by "name"
        .groupBy { it.listId }.toSortedMap()
}


