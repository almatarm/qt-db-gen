\#include "${className}.h"

#foreach( $cont in $entity.getConstructorMethodsImpl())
$cont
#end

#foreach( $field in $fields)
$field.getGetterMethodImpl()
${field.getSetterMethodImpl()}

#end