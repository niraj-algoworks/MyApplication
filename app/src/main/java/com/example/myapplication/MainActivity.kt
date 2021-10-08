package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.*
import org.hildan.krossbow.stomp.conversions.TextMessageConverter
import org.hildan.krossbow.stomp.conversions.kxserialization.*
import org.hildan.krossbow.stomp.conversions.withTextConversions
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    val URL = "https://dev.api.merlyn.org/ws"
    val myConverter = object : TextMessageConverter {
        override val mimeType: String = "application/json;charset=utf-8"

        override fun <T : Any> convertToString(body: T, bodyType: KClass<T>): String {
            TODO("your own object -> text conversion")
        }

        override fun <T : Any> convertFromString(body: String, bodyType: KClass<T>): T {
            TODO("your own text -> object conversion")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       MainScope().launch {
           bodyCommunication()
       }

    }


    suspend fun bodyCommunication(){


        val configMessage = ConfigMessage(MessageType.REFRESH_REQUEST,"The Content 2",null,"DEVICE1");

        val session = StompClient().connect(URL)
        val jsonStompSession = session.withJsonConversions() // adds convenience methods for kotlinx.serialization's conversions

        jsonStompSession.use { s ->
            s.convertAndSend("/app/device.refresh", configMessage, ConfigMessage.serializer())

            val messages: Flow<MyMessage> = s.subscribe("/topic/public", MyMessage.serializer())

            messages.collect {
                println("Received message from ${it.author}: ${it.content}")
            }
        }
    }

    suspend fun bodyCommunication1(){

        val configMessage = ConfigMessage(MessageType.REFRESH_REQUEST,"The Content 2",null,"DEVICE1");

        val session = StompClient().connect(URL)
        val jsonStompSession = session.withTextConversions(myConverter) // adds convenience methods for kotlinx.serialization's conversions

        jsonStompSession.use {

            it.convertAndSend("/some/destination", configMessage)

            val messages = session.subscribe<MyMessage>("/some/topic/destination")
            val firstMessage: MyMessage = messages.first()

            println("Received: $firstMessage")
        }
    }

}