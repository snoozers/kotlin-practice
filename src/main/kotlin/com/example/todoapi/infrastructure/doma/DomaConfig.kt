package com.example.todoapi.infrastructure.doma

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.NoCacheSqlFileRepository
import org.seasar.doma.jdbc.SqlFileRepository
import org.seasar.doma.jdbc.dialect.Dialect
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class DomaConfig {

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean
    fun domaConfiguration(): Config {
        return object : Config {
            override fun getDataSource(): DataSource {
                return TransactionAwareDataSourceProxy(dataSource)
            }

            override fun getDialect(): Dialect {
                return PostgresDialect()
            }

            override fun getSqlFileRepository(): SqlFileRepository {
                return NoCacheSqlFileRepository()
            }
        }
    }
}
