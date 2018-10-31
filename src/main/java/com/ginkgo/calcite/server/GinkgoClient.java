package com.ginkgo.calcite.server;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;


public class GinkgoClient {
    public void start(){
        try {

            String ip = "localhost"; //服务端的ip
            int port = 9090;//端口

//            TTransport socket = new TSocket(ip,port);
//            TProtocol protocol = new TBinaryProtocol(socket);
//            Ginkgo.Client client = new Ginkgo.Client(protocol);
//            socket.open();

            TTransport socket = new TSocket(ip,port);
            TTransport transport = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(transport);
            socket.open();

            Ginkgo.Client client = new Ginkgo.Client(protocol);

            List<TTableDescriptor> table_list=client.get_tables_meta();

            System.out.println( table_list.size());
            TTableDescriptor t = client.get_table_descriptor("T1");
            System.out.println( t.tableName);


            socket.close();
        }catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GinkgoClient demo = new GinkgoClient();
        demo.start();
    }

}