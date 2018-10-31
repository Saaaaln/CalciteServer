//package com.ginkgo.calcite.server;
//
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TFramedTransport;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//
//import java.util.List;
//
//public class MetaConnect {
//
//    private List<TTableDescriptor> table_list;
//
//    public MetaConnect(){
//        try {
//            table_list=getTables();
//        }
//        catch (Exception e){
//
//        }
//    }
//
//    public static List<TTableDescriptor> getTables() throws Exception {
//        int ginkgoPort=9090;
//
//        TTransport socket = new TSocket("localhost",ginkgoPort);
//        TTransport transport = new TFramedTransport(socket);
//        TProtocol protocol = new TBinaryProtocol(transport);
//        socket.open();
//
//        Ginkgo.Client client = new Ginkgo.Client(protocol);
//
//        List<TTableDescriptor> table_list=client.get_tables_meta();
//
//        socket.close();
//        return table_list;
//    }
//
//}
