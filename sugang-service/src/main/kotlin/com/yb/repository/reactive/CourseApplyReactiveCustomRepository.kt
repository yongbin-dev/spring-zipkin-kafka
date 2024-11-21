package com.yb.repository.reactive

import com.yb.domain.reactive.CourseApply
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOne
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.function.BiFunction

@Repository
class CourseApplyReactiveCustomRepository(private val client: DatabaseClient) {

    suspend fun findByCourseIdAndStudentId(courseId: Long, studentId: Long): CourseApply? = client
        .sql("SELECT * FROM course_apply WHERE course_id = $courseId and student_id = $studentId")
        .map(mapper())
        .one()
        .awaitFirstOrNull()

    suspend fun findByCourseId(courseId: Long): Flow<CourseApply> =
        client.sql("SELECT * FROM course_apply WHERE course_id = $courseId")
            .map(mapper())
            .flow()


    suspend fun countByCourseId(id: Long): Int {
        return client.sql("SELECT COUNT(*) FROM course_apply WHERE course_id = $id")
            .map { row -> row.get("COUNT(*)", Int::class.java) }
            .awaitOne()
    }

    private fun mapper(): BiFunction<Row, RowMetadata, CourseApply> = BiFunction { row, _ ->
        CourseApply(
            id = row["id", Long::class.java],
            courseId = row["course_id", Long::class.java],
            studentId = row["student_id", Long::class.java],
            createdAt = row["created_at", LocalDateTime::class.java]
        )
    }


//    suspend fun findById(id: String): Employee? = client
//        .sql("SELECT * FROM employee WHERE id = $1")
//        .bind(0, id)
//        .map(mapper())
//        .one()
//        .awaitFirstOrNull()
//
//    suspend fun save(employee: Employee) = this.client.sql("INSERT INTO  employee (name) VALUES (:name)")
//        .filter { statement, _ -> statement.returnGeneratedValues("id").execute() }
//        .bind("name", employee.name)
//        .fetch()
//        .first()
//        .map { r -> r["id"] as String }
//        .awaitFirstOrNull()
//
//    fun saveAll(data: List<Employee>): Flow<String> = client.inConnectionMany { connection ->
//        val statement =
//            connection.createStatement("INSERT INTO  employee (name) VALUES ($1)")
//                .returnGeneratedValues("id")
//        for (p in data) {
//            statement.bind(0, p.name).add()
//        }
//        Flux.from(statement.execute())
//            .flatMap { r -> r.map { row, _ -> row.get("id", String::class.java) } }
//    }.asFlow()
//
//    fun findAll() = client
//        .sql("SELECT * FROM employee")
//        .filter { statement, _ -> statement.fetchSize(10).execute() }
//        .map(mapper())
//        .flow()
//
//    private fun mapper(): BiFunction<Row, RowMetadata, Employee> = BiFunction { row, _ ->
//        Employee(
//            id = row["id", String::class.java],
//            name = row["name", String::class.java]
//        )
//    }


}