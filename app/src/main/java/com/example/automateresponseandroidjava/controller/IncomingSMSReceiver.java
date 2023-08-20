package com.example.automateresponseandroidjava.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class IncomingSMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Récupération des préférences stockées pour vérifier si la réponse automatique est activée
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean autoReplyEnabled = sharedPreferences.getBoolean("autoReplyEnabled", false);

        if (!autoReplyEnabled) {
            return;
        }

        // Récupération du message SMS entrant du bundle de l'intent
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        // Les "PDUs" sont les "Protocol Data Units", qui sont la base des messages SMS
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        SmsMessage sms = messages[0];

        // Récupérez le contenu du message et le numéro de l'expéditeur
        String messageBody = sms.getMessageBody();
        String sender = sms.getOriginatingAddress();

        // Envoi d'une réponse automatique à l'expéditeur
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sender, null, "Je ne suis pas disponible, je vous recontacterai", null, null);
    }
}

