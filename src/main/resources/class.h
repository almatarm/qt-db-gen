#pragma once

#foreach ($inc in $entity.getIncludes())
\#include $inc
#end
\#include "$globalHeaderFile"

namespace $namespace {

class $export_library $className {
public:
#foreach( $cont in $entity.getConstructorMethods())
    $cont
#end

#foreach( $field in $fields)
    $field.getGetterMethod()
    ${field.getSetterMethod()}
#end

private:
#foreach( $field in $fields)
    $field.type ${field.name}_;
#end
};

}