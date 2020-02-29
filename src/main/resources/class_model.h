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

private:
    bool isIndexValid(const QModelIndex& index) const;
    bool isWritableRole(int role) const;

private:
    DatabaseManager& dbMgr_;
    std::unique_ptr<std::vector<std::unique_ptr<${className}>>> ${classNameVars}_;
};

}
