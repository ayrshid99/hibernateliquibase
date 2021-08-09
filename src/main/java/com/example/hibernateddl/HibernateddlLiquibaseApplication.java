package com.example.hibernateddl;

import liquibase.CatalogAndSchema;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.core.MariaDBDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.ObjectChangeFilter;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.ext.hibernate.database.HibernateSpringPackageDatabase;
import liquibase.ext.hibernate.database.connection.HibernateConnection;
import liquibase.integration.commandline.CommandLineUtils;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.*;
import liquibase.util.StringUtil;
import org.hibernate.dialect.MariaDB53Dialect;
import org.hibernate.dialect.MariaDBDialect;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SpringBootApplication
public class HibernateddlLiquibaseApplication {

    public static void main(String[] args) throws Exception {

        String diffChangeLogFile = "/tmp/abc.xml";
        File outFile = new File(diffChangeLogFile);
        Class.forName("org.mariadb.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sample_database", "root", "");

        connection.getMetaData().getDriverName();
        Database database = new MariaDBDatabase();
        database.setConnection(new JdbcConnection(connection));
        final Liquibase liquibaseUpdate = new Liquibase(outFile.toString(), new FileSystemResourceAccessor(File.listRoots()), database);

        Database hibernateDatabase = new HibernateSpringPackageDatabase();
        hibernateDatabase.setDefaultSchemaName("sample_database");
        hibernateDatabase.setDefaultCatalogName("sample_database");
        hibernateDatabase.setConnection(new JdbcConnection(new HibernateConnection("hibernate:spring:" + "com.example.hibernateddl" + "?dialect=" + MariaDBDialect.class.getName(), new ClassLoaderResourceAccessor())));
        ObjectChangeFilter objectChangeFilter = null;

        //    liquibase.update(changesToApply, new Contexts(contexts), new LabelExpression(labels));

        final Liquibase liquibaseUpdate = new Liquibase(outFile.toString(), new FileSystemResourceAccessor(File.listRoots()), database);
        //     liquibaseUpdate.rollback(2,"");

//        CompareControl.SchemaComparison[] schemaComparisons = createSchemaComparisons(database);

        try {
            DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, true, null);
            diffOutputControl.setObjectChangeFilter(objectChangeFilter);
            CommandLineUtils.doDiffToChangeLog(outFile.toString(), hibernateDatabase, database, diffOutputControl,
                    objectChangeFilter, StringUtil.trimToNull(null),null);


            liquibaseUpdate.update("");


        } catch (IOException | ParserConfigurationException e) {
            throw new LiquibaseException(e);
        }

//        DiffResult schemaDiff = liquibase.diff( database, hibernateDatabase,getCompareControl());
//
//
//        File outFile = new File("/change_log/" + System.currentTimeMillis() + ".xml");
//
//        if (!new File(Paths.get("/change_log").toUri()).exists()) {
//            Files.createDirectory(Paths.get("/change_log"));
//        }
//        outFile.createNewFile();
//        OutputStream outChangeLog = new FileOutputStream(outFile);
//        HibernateddlLiquibaseApplication.ignoreDatabaseChangeLogTable(schemaDiff);
//        String changeLogString = toChangeLog(schemaDiff);
//        outChangeLog.write(changeLogString.getBytes("UTF-8"));
//        outChangeLog.close();
//        System.out.println(changeLogString);
//
//
//        final Liquibase liquibaseWithFile = new Liquibase(outFile.toString(), new FileSystemResourceAccessor(File.listRoots()), database);
//
//
//        try {
//            final ResultSet resultSet = connection.prepareStatement("select dateexecuted from databasechangelog order by dateexecuted desc limit 1,1"
//            ).executeQuery();
//
//            if(resultSet.next()){
//
//                final Date dateexecuted=resultSet.getDate("dateexecuted");
//                liquibaseWithFile.rollback(8,(String) null);
//                liquibaseWithFile.rollback(dateexecuted,(String) null);
//
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (LiquibaseException e) {
//            e.printStackTrace();
//        }
//
//
//        liquibaseWithFile.update((String) null);

    }

    public static String toChangeLog(DiffResult diffResult) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, "UTF-8");
        DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult,
                new DiffOutputControl().setIncludeCatalog(false).setIncludeSchema(false));
        diffToChangeLog.print(printStream);
        printStream.close();
        return out.toString("UTF-8");
    }

    private static CompareControl.SchemaComparison[] createSchemaComparisons(Database database) {
        CompareControl.ComputedSchemas computedSchemas = CompareControl.computeSchemas(
                null,
                null,
                null,
                null, null,
                null, null,
                database);

        CompareControl.SchemaComparison[] finalSchemaComparisons = computedSchemas.finalSchemaComparisons;
        return finalSchemaComparisons;
    }

    public static void ignoreDatabaseChangeLogTable(DiffResult diffResult)
            throws Exception {
        Set<Table> unexpectedTables = diffResult
                .getUnexpectedObjects(Table.class);
        for (Iterator<Table> iterator = unexpectedTables.iterator(); iterator
                .hasNext(); ) {
            Table table = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(table.getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(table.getName()))
                diffResult.getUnexpectedObjects().remove(table);
        }
        Set<Table> missingTables = diffResult
                .getMissingObjects(Table.class);
        for (Iterator<Table> iterator = missingTables.iterator(); iterator
                .hasNext(); ) {
            Table table = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(table.getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(table.getName()))
                diffResult.getMissingObjects().remove(table);
        }
        Set<Column> unexpectedColumns = diffResult.getUnexpectedObjects(Column.class);
        for (Iterator<Column> iterator = unexpectedColumns.iterator(); iterator.hasNext(); ) {
            Column column = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(column.getRelation().getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(column.getRelation().getName()))
                diffResult.getUnexpectedObjects().remove(column);
        }
        Set<Column> missingColumns = diffResult.getMissingObjects(Column.class);
        for (Iterator<Column> iterator = missingColumns.iterator(); iterator.hasNext(); ) {
            Column column = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(column.getRelation().getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(column.getRelation().getName()))
                diffResult.getMissingObjects().remove(column);
        }
        Set<Index> unexpectedIndexes = diffResult.getUnexpectedObjects(Index.class);
        for (Iterator<Index> iterator = unexpectedIndexes.iterator(); iterator.hasNext(); ) {
            Index index = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(index.getTable().getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(index.getTable().getName()))
                diffResult.getUnexpectedObjects().remove(index);
        }
        Set<Index> missingIndexes = diffResult.getMissingObjects(Index.class);
        for (Iterator<Index> iterator = missingIndexes.iterator(); iterator.hasNext(); ) {
            Index index = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(index.getTable().getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(index.getTable().getName()))
                diffResult.getMissingObjects().remove(index);
        }
        Set<PrimaryKey> unexpectedPrimaryKeys = diffResult.getUnexpectedObjects(PrimaryKey.class);
        for (Iterator<PrimaryKey> iterator = unexpectedPrimaryKeys.iterator(); iterator.hasNext(); ) {
            PrimaryKey primaryKey = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(primaryKey.getTable()
                    .getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(primaryKey.getTable().getName()))
                diffResult.getUnexpectedObjects().remove(primaryKey);
        }
        Set<PrimaryKey> missingPrimaryKeys = diffResult.getMissingObjects(PrimaryKey.class);
        for (Iterator<PrimaryKey> iterator = missingPrimaryKeys.iterator(); iterator.hasNext(); ) {
            PrimaryKey primaryKey = iterator.next();
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(primaryKey.getTable().getName())
                    || "DATABASECHANGELOG".equalsIgnoreCase(primaryKey.getTable().getName()))
                diffResult.getMissingObjects().remove(primaryKey);
        }
    }





    public static void ignorealterSequence(DiffResult result) {

        result.getMissingObjects().stream().forEach(item -> {
            System.out.println(item.getClass());
        });

    }

}
