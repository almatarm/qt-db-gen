#pragma once

\#include <QAbstractListModel>
\#include <QHash>
\#include <vector>
\#include <memory>

\#include "$globalHeaderFile"
\#include "${className}.h"
\#include "DatabaseManager.h"

class QSqlDatabase;

namespace $namespace {

class $export_library ${className}Model  : public QAbstractListModel
{
    Q_OBJECT
public:
    enum Roles {
#foreach( $field in $fields)
        ${field.nameToRole()}#if( $foreach.first ) = Qt::UserRole + 1#end,
#end
    };

    ${className}Model(QObject* parent = nullptr);

    QModelIndex add${className}(const ${className}& ${classNameVar});

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;
    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
    bool setData(const QModelIndex& index, const QVariant& value, int role) override;
    bool removeRows(int row, int count, const QModelIndex& parent) override;
    QHash<int, QByteArray> roleNames() const override;

#foreach( $fk in $forignKeys)
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkClass = $fk.entity.className )
    void set${Util.capitalizeFirstLetter($fkName)}(int $fkName);
    void clear$fkClass();
#end

#if( !$forignKeys.isEmpty() )
public slots:
#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkType = $fk.key.type )
#if( $fk.fieldName == 'parent_id')
#set ( $methodPostFix = ${Noun.pluralOf(${className})} )
#else
#set ( $methodPostFix = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#end
    void delete${methodPostFix}($fkType $fkName);
#end
#end

private:
    bool isIndexValid(const QModelIndex& index) const;
    bool isWritableRole(int role) const;
#if( !$forignKeys.isEmpty() )
#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkType = $fk.key.type )
#if( $fk.fieldName == 'parent_id')
#set ( $argName = $fkName + ' = ' + ${fk.key.defaultValue})
#set ( $methodPostFix = ${Noun.pluralOf(${className})} )
#else
#set ( $argName = $fkName )
#set ( $methodPostFix = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#end
    void load${methodPostFix}($fkType $argName);
#end
#end

private:
    DatabaseManager& dbMgr_;
#foreach( $fk in $forignKeys)
#set ( $fkName = $Util.toCamelCase($fk.fieldName) + "_" )
#set ( $fkType = $fk.key.type )
    $fkType $fkName;
#end
    std::unique_ptr<std::vector<std::unique_ptr<${className}>>> ${classNameVars}_;
};

}
