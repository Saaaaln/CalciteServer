//package com.ginkgo.calcite.server;
//
//import org.apache.calcite.linq4j.tree.Expression;
//import org.apache.calcite.schema.*;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class GinkgoSchema implements Schema {
//
//    final private MetaConnect metaConnect;
//
//    @Override
//    public Table getTable(String string) {
////        Table table = metaConnect.getTable(string);
//        return table;
//    }
//
//    @Override
//    public Set<String> getTableNames() {
//        Set<String> tableSet = metaConnect.getTables();
//        return tableSet;
//    }
//
//    @Override
//    public Collection<Function> getFunctions(String string) {
//        Collection<Function> functionCollection = new HashSet<Function>();
//        return functionCollection;
//    }
//
//    @Override
//    public Set<String> getFunctionNames() {
//        Set<String> functionSet = new HashSet<String>();
//        return functionSet;
//    }
//
//    @Override
//    public Schema getSubSchema(String string) {
//        return null;
//    }
//
//    @Override
//    public Set<String> getSubSchemaNames() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Expression getExpression(SchemaPlus sp, String string) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isMutable() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    void updateMetaData(String schema, String table) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Schema snapshot(SchemaVersion sv) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}