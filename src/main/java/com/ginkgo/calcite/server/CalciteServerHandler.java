package com.ginkgo.calcite.server;

import com.thrift.calciteserver.CalciteServer;
import com.thrift.calciteserver.InvalidParseRequest;
import com.thrift.calciteserver.TPlanResult;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.plan.*;
import org.apache.calcite.plan.volcano.AbstractConverter;
import org.apache.calcite.prepare.CalcitePrepareImpl;
import org.apache.calcite.rel.RelDistributionTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.externalize.RelJsonWriter;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.sql.SqlExplainFormat;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.String;
import java.sql.Time;
import java.util.*;

public class CalciteServerHandler implements CalciteServer.Iface{
    final static Logger GLOGGER = LoggerFactory.getLogger(CalciteServerHandler.class);
    static FrameworkConfig fconfig= null;
    public void ping() throws TException {}

    public void shutdown() throws TException{}

    public TPlanResult sql2Plan(String user, String passwd, String catalog, String sql_text, boolean legacySyntax, boolean isexplain) throws InvalidParseRequest, TException{
        long timer = System.currentTimeMillis();
        TPlanResult result = new TPlanResult();
        try {
        result.plan_result=executeQuery(fconfig,sql_text,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.execution_time_ms = System.currentTimeMillis() - timer;
        return result;
        
    }

    public void updateMetadata(String catalog, String table) throws TException{}

    public static void createSchema() throws Exception {
        final SchemaPlus rootSchema = Frameworks.createRootSchema(true);
        SchemaPlus schema = rootSchema.add("x", new AbstractSchema());
//        schema.add("MYTABLE", table);
        List<TTableDescriptor> tables=getTables();
        for (TTableDescriptor table : tables) {
            System.out.println(table.tableName);
            GinkgoTable tb=new GinkgoTable(table);
            rootSchema.add(table.tableName, tb);
        }

        List<RelTraitDef> traitDefs = new ArrayList<>();
        traitDefs.add(ConventionTraitDef.INSTANCE);
        traitDefs.add(RelDistributionTraitDef.INSTANCE);
        SqlParser.Config parserConfig =
                SqlParser.configBuilder(SqlParser.Config.DEFAULT)
                        .setCaseSensitive(false)
                        .build();

        fconfig = Frameworks.newConfigBuilder()
                .parserConfig(parserConfig)
                .defaultSchema(schema)
                .traitDefs(traitDefs)
                // define the rules you want to apply
                .ruleSets(
                        RuleSets.ofList(AbstractConverter.ExpandConversionRule.INSTANCE))
                .programs(Programs.ofRules(Programs.RULE_SET))
                .build();
    }
    private String executeQuery(FrameworkConfig config,
                              @SuppressWarnings("SameParameterValue") String query, boolean debug)
            throws RelConversionException, SqlParseException, ValidationException {
        Planner planner = Frameworks.getPlanner(config);
        if (debug) {
            System.out.println("Query:" + query);
        }
        SqlNode n = planner.parse(query);
        n = planner.validate(n);
        RelNode root = planner.rel(n).project();
        if (debug) {
            System.out.println(
                    RelOptUtil.dumpPlan("-- Logical Plan", root, SqlExplainFormat.TEXT,
                            SqlExplainLevel.DIGEST_ATTRIBUTES));
        }
        RelOptCluster cluster = root.getCluster();
        final RelOptPlanner optPlanner = cluster.getPlanner();

        RelTraitSet desiredTraits  =
                cluster.traitSet().replace(EnumerableConvention.INSTANCE);
        final RelNode newRoot = optPlanner.changeTraits(root, desiredTraits);
        if (debug) {
            System.out.println(
                    RelOptUtil.dumpPlan("-- Mid Plan", newRoot, SqlExplainFormat.TEXT,
                            SqlExplainLevel.DIGEST_ATTRIBUTES));
        }
        optPlanner.setRoot(newRoot);
        RelNode bestExp = optPlanner.findBestExp();
        if (debug) {
            System.out.println(
                    RelOptUtil.dumpPlan("-- Best Plan", bestExp, SqlExplainFormat.TEXT,
                            SqlExplainLevel.DIGEST_ATTRIBUTES));
        }

        final RelJsonWriter writer = new RelJsonWriter();
        bestExp.explain(writer);
        final String json = writer.asString();
        return json;
    }

//    public static Set<String> getTables() throws Exception {
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
//        Set<String> tables=new HashSet<>();
//
//        for (int i=0;i<table_list.size();++i){
//            tables.add(table_list.get(i).tableName);
//        }
//
//        socket.close();
//
//        return tables;
//    }

public static List<TTableDescriptor> getTables() throws Exception {
    int ginkgoPort=9090;

    TTransport socket = new TSocket("localhost",ginkgoPort);
    TTransport transport = new TFramedTransport(socket);
    TProtocol protocol = new TBinaryProtocol(transport);
    socket.open();

    Ginkgo.Client client = new Ginkgo.Client(protocol);

    List<TTableDescriptor> table_list=client.get_tables_meta();

    return table_list;
}
}
