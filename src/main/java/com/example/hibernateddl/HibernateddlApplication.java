package com.example.hibernateddl;

import ch.qos.logback.core.pattern.ConverterUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.HashMap;

@SpringBootApplication
public class HibernateddlApplication {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        String arr[] = {"abc", "def"};


        BaseType baseType1=new D1();
        BaseType baseType2=new D2();
        final HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        BeanUtils.copyProperties(baseType2,baseType1);

        StandardServiceRegistryBuilder standardRegistryBuilder = new StandardServiceRegistryBuilder();
        standardRegistryBuilder
                .applySetting("hibernate.dialect", "org.hibernate.dialect.MariaDB53Dialect")
                .applySetting("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver")
                .applySetting("hibernate.connection.url", "jdbc:mariadb://localhost:3306/sample_database")
                .applySetting("hibernate.connection.username", "root")
                .applySetting("hibernate.connection.password", "");
        final ServiceRegistry standardRegistry = standardRegistryBuilder.build();

        // let's define what's the class to be scanned as entity
        MetadataSources sources = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Sample.class);
        MetadataBuilder metadataBuilder = sources.getMetadataBuilder();

        Metadata metadata = metadataBuilder.build();

        // based on the metadata we are creating the database schema
        SchemaExport schemaExport = new SchemaExport();
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaExport.setOutputFile("script.sql");
//        schemaExport.create( EnumSet.of( TargetType.DATABASE ), metadata);
        schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata);
        if (schemaExport.getExceptions() != null && !schemaExport.getExceptions().isEmpty()) {
            System.err.println("[ERROR] exception during schema creation: " + schemaExport.getExceptions());
        }
    }

}
