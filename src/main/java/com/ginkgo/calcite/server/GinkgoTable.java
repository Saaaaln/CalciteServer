package com.ginkgo.calcite.server;

import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;

public class GinkgoTable implements Table {

    private final TTableDescriptor table_;

    public GinkgoTable(TTableDescriptor table) {
        table_ = table;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory rdtf) {
        RelDataTypeFactory.FieldInfoBuilder builder = rdtf.builder();
        for (Attribute tct : table_.attributes ) {
            builder.add(tct.attrName, createType(tct, rdtf));
        }
        return builder.build();
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.UNKNOWN;
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.TABLE;
    }

    private RelDataType createType(Attribute att, RelDataTypeFactory typeFactory) {
        RelDataType cType = getRelDataType(att.attrType,  typeFactory);
            return cType;
    }

    // Convert our TDataumn type in to a base calcite SqlType
    // todo confirm whether it is ok to ignore thinsg like lengths
    // since we do not use them on the validator side of the calcite 'fence'
    private RelDataType getRelDataType(column_type att, RelDataTypeFactory typeFactory) {
        switch (att) {
            case INT:
                return typeFactory.createSqlType(SqlTypeName.INTEGER);
            case FLOAT:
                return typeFactory.createSqlType(SqlTypeName.FLOAT);
            case DOUBLE:
                return typeFactory.createSqlType(SqlTypeName.DOUBLE);
            case STRING:
                return typeFactory.createSqlType(SqlTypeName.VARCHAR, 100);
            default:
                throw new AssertionError(att.name());
        }
    }

    @Override
    public boolean isRolledUp(String string) {
        //will set to false by default
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String string, SqlCall sc, SqlNode sn, CalciteConnectionConfig ccc) {
        throw new UnsupportedOperationException("rolledUpColumnValidInsideAgg Not supported yet.");
    }
}