\#include "DatabaseManager.h"

\#include <QSqlDatabase>
\#include <QDebug>
\#include <QSqlError>
\#include <QSqlQuery>


void $namespace::DatabaseManager::debugQuery(const QSqlQuery& query)
{
    if (query.lastError().type() == QSqlError::ErrorType::NoError) {
       qDebug() << "Query OK:"  << query.lastQuery();
    } else {
       qWarning() << "Query KO:" << query.lastError().text();
       qWarning() << "Query text:" << query.lastQuery();
    }
}

$namespace::DatabaseManager& $namespace::DatabaseManager::instance()
{
    static DatabaseManager singleton;
    return singleton;
}

$namespace::DatabaseManager::DatabaseManager(const QString& path) :
    database_(new QSqlDatabase(QSqlDatabase::addDatabase("QSQLITE"))),
#foreach ($entity in $entities)
    ${Util.decapitalizeFirstLetter(${entity.className})}Dao(*database_)#if( $foreach.hasNext ), #end

#end
{
    database_->setDatabaseName(path);

    bool openStatus = database_->open();
    qDebug() << "Database connection: " << (openStatus ? "OK" : "Error");

#foreach ($entity in $entities)
    ${Util.decapitalizeFirstLetter(${entity.className})}Dao.init();
#end
}

$namespace::DatabaseManager::~DatabaseManager()
{
    database_->close();
}
