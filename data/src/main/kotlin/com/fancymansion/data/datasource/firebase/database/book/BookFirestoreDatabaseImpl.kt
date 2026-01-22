package com.fancymansion.data.datasource.firebase.database.book

import com.google.firebase.firestore.FirebaseFirestore


class BookFirestoreDatabaseImpl(
    private val firestore: FirebaseFirestore
) : BookFirestoreDatabase {

}