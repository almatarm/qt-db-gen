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

    void remove${Noun.pluralOf(${className})}For${fkClassName}($fkType $fkName) const;
    std::unique_ptr<std::vector<std::unique_ptr<${className}>>> ${Noun.pluralOf(${classNameVar})}For${fkClassName}($fkType $fkName) const;

private:
    QSqlDatabase& database_;
};

}
