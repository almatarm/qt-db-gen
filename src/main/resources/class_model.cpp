\#include "${className}Model.h"

using namespace std;

${namespace}::${className}Model::${className}Model(QObject* parent) :
    QAbstractListModel(parent),
    dbMgr_(DatabaseManager::instance()),
    ${classNameVars}_(dbMgr_.${classDaoVar}.${classNameVars}()) {
}


QModelIndex ${namespace}::${className}Model::add${className}(const ${namespace}::${className}& ${classNameVar}) {
    int rowIndex = rowCount();
    beginInsertRows(QModelIndex(), rowIndex, rowIndex);
    unique_ptr<${className}> new${className}(new ${className}(${classNameVar}));
    dbMgr_.${classNameVar}Dao.add${className}(*new${className});
    ${classNameVars}_->push_back(move(new${className}));
    endInsertRows();
    return index(rowIndex, 0);
}

int ${namespace}::${className}Model::rowCount(const QModelIndex& parent) const {
    Q_UNUSED(parent)
    return int(${classNameVars}_->size());
}

QVariant ${namespace}::${className}Model::data(const QModelIndex& index, int role) const {
    if (!isIndexValid(index)) {
        return QVariant();
    }
    const ${className}& ${classNameVar} = *${classNameVars}_->at(std::size_t(index.row()));

    switch (role) {
#foreach( $field in $fields)
#foreach( $role in $field.roles )
        case ${role}:
#end
        case Roles::${field.nameToRole()}:
            return ${classNameVar}.${field.name}();

#end

        default:
            return QVariant();
    }
}

bool ${namespace}::${className}Model::setData(const QModelIndex& index, const QVariant& value, int role) {
    if (!isIndexValid(index) || !isWritableRole(role)) {
        return false;
    }
    ${className}& ${classNameVar} = *${classNameVars}_->at(std::size_t(index.row()));
    switch(role) {
#foreach( $field in $fields)
        case Roles::${field.nameToRole()}:
            ${classNameVar}.set${Util.capitalizeFirstLetter(${field.name})}(value.to${field.qVariantTo()}());
            break;
#end
    }
    dbMgr_.${classNameVar}Dao.update${className}(${classNameVar});
    emit dataChanged(index, index);
    return true;
}

bool ${namespace}::${className}Model::removeRows(int row, int count, const QModelIndex& parent)
{
    if (row < 0
            || row >= rowCount()
            || count < 0
            || (row + count) > rowCount()) {
        return false;
    }
    beginRemoveRows(parent, row, row + count - 1);
    int countLeft = count;
    while (countLeft--) {
        const ${className}& ${classNameVar} = *${classNameVars}_->at(std::size_t(row + countLeft));
        dbMgr_.${classNameVar}Dao.remove${className}(${classNameVar}.id());
    }
    ${classNameVars}_->erase(${classNameVars}_->begin() + row,
                  ${classNameVars}_->begin() + row + count);
    endRemoveRows();
    return true;
}

QHash<int, QByteArray> ${namespace}::${className}Model::roleNames() const
{
    QHash<int, QByteArray> roles;
#foreach( $field in $fields)
    roles[Roles::${field.nameToRole()}] = "${field.name}";
#end
    return roles;
}

bool ${namespace}::${className}Model::isIndexValid(const QModelIndex &index) const
{
    if (index.row() < 0
            || index.row() >= rowCount()
            || !index.isValid()) {
        return false;
    }
    return true;
}

bool ${namespace}::${className}Model::isWritableRole(int role) const
{
    return ${entity.getWritableFields()}
}