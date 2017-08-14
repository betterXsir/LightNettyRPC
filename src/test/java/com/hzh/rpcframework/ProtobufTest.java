package com.hzh.rpcframework;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.hzh.rpcframework.serialize.protobuf.AddressBookProtos;
import com.hzh.rpcframework.serialize.protobuf.ErrorStatusOuterClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.Executor;

public class ProtobufTest {
    static AddressBookProtos.Person PromptForAddress(BufferedReader stdin, PrintStream stdout) throws IOException {
        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();

        stdout.print("Enter person ID: ");
        person.setId(Integer.valueOf(stdin.readLine()));

        stdout.print("Enter name: ");
        person.setName(stdin.readLine());

        stdout.print("Enter email address (blank for none)");
        String email = stdin.readLine();
        if(!email.isEmpty())
            person.setEmail(stdin.readLine());
        while(true){
            stdout.print("Enter phone number (or leave blank to finish)");
            String number = stdin.readLine();
            if(number.length() == 0){
                break;
            }
            AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder();
            phoneNumber.setNumber(number);
            stdout.print("Is this a mobile, home, or work phone?");
            String type = stdin.readLine();
            if(type.equals("mobile")){
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
            }
            else if(type.equals("home")){
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
            }
            else{
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.WORK);
            }
            person.addPhones(phoneNumber);
        }
        return person.build();
    }

    public static void main(String[] args) throws Exception{
        ErrorStatusOuterClass.ErrorStatus.Builder errorStatus = ErrorStatusOuterClass.ErrorStatus.newBuilder();
        errorStatus.setMessage("Class Not Found.");
        errorStatus.addDetails(Any.pack());
    }
}
