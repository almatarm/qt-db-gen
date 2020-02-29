#pragma once

\#include <memory>
\#include <vector>
\#include "${className}.h"
#foreach ($inc in $includes)
\#include $inc
#end

class QSqlDatabase;

namespace $namespace {

class ${className}Dao {
public:
    ${className}Dao(QSqlDatabase& database);
    void init() const;

    void add${className}(${className}& ${classNameVar}) const;
    void update${className}(const ${className}& ${classNameVar}) const;
    void remove${className}(int id) const;


    std::unique_ptr<std::vector<std::unique_ptr<${className}>>> ${Noun.pluralOf(${classNameVar})}() const;
#if( !$forignKeys.isEmpty() )
#foreach( $fk in $forignKeys)
#set ( $fkClassName = $fk.entity.className )
#set ( $fkName = $fk.fieldName )
#set ( $fkType = $fk.key.type )

#if( $fkName == 'parent_id')
#set ( $argName = ${classNameVar} + '_id' )
#set ( $methodPostFix = ${classNameVar} + 'Children' )
#else
#set ( $argName = $fkName )
#set ( $methodPostFix = ${Noun.pluralOf(${className})} + 'For' + ${fkClassName} )
#end
    void remove${Util.capitalizeFirstLetter($methodPostFix)}($fkType $argName) const;
    std::unique_ptr<std::vector<std::unique_ptr<${className}>>> ${Util.decapitalizeFirstLetter($methodPostFix)}($fkType $argName) const;

#end
#end
private:
    QSqlDatabase& database_;
};

}
