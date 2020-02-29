\#include "${className}Dao.h"

\#include <QSqlDatabase>
\#include <QSqlQuery>
\#include <QVariant>

\#include "DatabaseManager.h"

using namespace std;

${namespace}::${className}Dao::${className}Dao(QSqlDatabase& database) :
    database_(database)
{
}


void ${namespace}::${className}Dao::init() const {
    if (!database_.tables().contains("${table}")) {
        QSqlQuery query(database_);
        query.exec(
            $create_table
            );
        DatabaseManager::debugQuery(query);
    }
}

void ${namespace}::${className}Dao::add${className}(${className}& $classNameVar) const {
    QSqlQuery query(database_);
    query.prepare(
        $insert_statement
    );
#foreach( $field in $fields)
    query.bindValue(":$field.name", ${classNameVar}.${field.name}());
#end
    query.exec();
    ${classNameVar}.setId(query.lastInsertId().toInt());
    DatabaseManager::debugQuery(query);
}

void ${namespace}::${className}Dao::update${className}(const ${className}& $classNameVar) const {
    QSqlQuery query(database_);
    query.prepare(
        $update_statement
    );
#foreach( $field in $fields)
    query.bindValue(":$field.name", ${classNameVar}.${field.name}());
#end
    query.exec();
    DatabaseManager::debugQuery(query);
}

void ${namespace}::${className}Dao::remove${className}(int id) const {
    QSqlQuery query(database_);
    query.prepare("DELETE FROM $table WHERE id = (:id)");
    query.bindValue(":id", id);
    query.exec();
    DatabaseManager::debugQuery(query);
}

unique_ptr<vector<unique_ptr<${namespace}::${className}>>> ${namespace}::${className}Dao::${Noun.pluralOf(${classNameVar})}() const {
    QSqlQuery query("SELECT * FROM $table", database_);
    query.exec();
    unique_ptr<vector<unique_ptr<${className}>>> list(new vector<unique_ptr<${className}>>());
    while(query.next()) {
        unique_ptr<${className}> ${classNameVar}(new ${className}());
#foreach( $field in $fields)
        $field.qVariantConvertor(${classNameVar})
#end
        list->push_back(move(${classNameVar}));
    }
    return list;
}
