package com.ecomapp.admin.Modules

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun getFireStoreDBInstance() : FirebaseFirestore{
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun getFireStorageInstance() : FirebaseStorage{
        return Firebase.storage
    }
}