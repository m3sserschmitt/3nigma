package com.example.enigma.data

import com.example.enigma.data.database.ContactEntity
import com.example.enigma.data.database.ContactsDao
import com.example.enigma.data.database.KeyPairEntity
import com.example.enigma.data.database.KeyPairsDao
import com.example.enigma.data.database.MessageEntity
import com.example.enigma.data.database.MessagesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val contactsDao: ContactsDao,
    private val messagesDao: MessagesDao,
    private val keysDao: KeyPairsDao
){
    fun getContacts() : Flow<List<ContactEntity>>
    {
        return contactsDao.getAll()
    }

    fun getContact(address: String) : Flow<ContactEntity>
    {
        return contactsDao.getContact(address)
    }

    suspend fun insertContact(contactEntity: ContactEntity)
    {
        contactsDao.insert(contactEntity)
    }

    fun getConversation(chatId: String) : Flow<List<MessageEntity>>
    {
        return messagesDao.getConversation(chatId)
    }

    suspend fun insertMessage(messageEntity: MessageEntity)
    {
        messagesDao.insert(messageEntity)
    }

    suspend fun insertMessages(messageEntities: List<MessageEntity>)
    {
        messagesDao.insert(messageEntities)
    }

    suspend fun markConversationAsUnread(address: String)
    {
        contactsDao.markConversationAsUnread(address)
    }

    suspend fun markConversationAsRead(address: String)
    {
        contactsDao.markConversationAsRead(address)
    }

    suspend fun insertKeyPair(keyPairEntity: KeyPairEntity)
    {
        keysDao.insert(keyPairEntity)
    }

    fun isKeyAvailable(): Flow<Boolean>
    {
        return keysDao.isKeyAvailable()
    }

    fun getKeys(): Flow<KeyPairEntity>
    {
        return keysDao.getLastKeys()
    }

    fun getPublicKey(): Flow<String>
    {
        return keysDao.getPublicKey()
    }

    fun getAddress(): Flow<String>
    {
        return keysDao.getAddress()
    }
}
