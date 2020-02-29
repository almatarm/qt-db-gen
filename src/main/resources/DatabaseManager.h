#pragma once

\#include <memory>
\#include <QString>
#foreach ($entity in $entities)
\#include "${entity.className}Dao.h"
#end

class QSqlQuery;
class QSqlDatabase;

namespace $namespace {
const QString DATABASE_FILENAME = "$database_filename";

class DatabaseManager
{
public:
    static void debugQuery(const QSqlQuery& query);

    static DatabaseManager& instance();
    ~DatabaseManager();

protected:
    DatabaseManager(const QString& path = DATABASE_FILENAME);
    DatabaseManager& operator=(const DatabaseManager& rhs);

private:
    std::unique_ptr<QSqlDatabase> database_;

public:
#foreach ($entity in $entities)
    const ${entity.className}Dao ${Util.decapitalizeFirstLetter(${entity.className})}Dao;
#end
};

}