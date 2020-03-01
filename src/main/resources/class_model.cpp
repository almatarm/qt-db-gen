\#include "${className}Model.h"

using namespace std;

${namespace}::${className}Model::${className}Model(QObject* parent) :
    QAbstractListModel(parent),
    dbMgr_(DatabaseManager::instance()),
#if( $forignKeys.isEmpty() )
    ${classNameVars}_(dbMgr_.${classDaoVar}.${classNameVars}()) {
#else
#foreach( $fk in $forignKeys)
#set ( $fkName = $Util.toCamelCase($fk.fieldName) + "_" )
#set ( $fkType = $fk.key.type )
    ${fkName}(${fk.key.defaultValue}),
#end
    ${classNameVars}_(new std::vector<std::unique_ptr<${className}>>()) {
#end
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

#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkClass = $fk.entity.className )
#if( $fk.fieldName == 'parent_id')
#set ( $loadObjects = ${Noun.pluralOf(${className})} )
#else
#set ( $loadObjects = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#end

void ${namespace}::${className}Model::set${Util.capitalizeFirstLetter($fkName)}(int $fkName) {
    beginResetModel();
    ${fkName}_ = $fkName;
    load${loadObjects}($fkName);
    endResetModel();
}

void ${namespace}::${className}Model::clear$fkClass() {
    set${Util.capitalizeFirstLetter($fkName)}($fk.key.defaultValue);
}

#end

#if( !$forignKeys.isEmpty() )
#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkType = $fk.key.type )
#if( $fk.fieldName == 'parent_id')
#set ( $methodPostFix = ${Noun.pluralOf(${className})} )
#set ( $daoMethod =  ${Util.capitalizeFirstLetter(${classNameVar})} + 'Children' )
#else
#set ( $methodPostFix = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#set ( $daoMethod = $methodPostFix )
#end
void ${namespace}::${className}Model::delete${methodPostFix}($fkType $fkName) {
    dbMgr_.${classNameVar}Dao.remove${daoMethod}($fkName);
    clear${fkClass}();
}

#end
#end

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

#if( !$forignKeys.isEmpty() )
#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $Util.toCamelCase($fk.fieldName) )
#set ( $fkType = $fk.key.type )
#if( $fk.fieldName == 'parent_id')
#set ( $methodPostFix = ${Noun.pluralOf(${className})} )
#set ( $daoMethod =  ${classNameVar} + 'Children' )
#else
#set ( $methodPostFix = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#set ( $daoMethod = ${Util.decapitalizeFirstLetter($methodPostFix)} )
#end
void ${namespace}::${className}Model::load${methodPostFix}($fkType $fkName)
{
    if ($fkName <= 0) {
        ${classNameVars}_.reset(new vector<unique_ptr<$className>>());
        return;
    }
    ${classNameVars}_ = dbMgr_.${classNameVar}Dao.${daoMethod}($fkName);
}

#end
#end