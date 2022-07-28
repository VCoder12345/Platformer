import re

path = input("path: ")
name = input("name: ")
components = input("components: ").split(", ")
if(components[0] == ""):
    components = []
createComp = input("create component: ") == "yes"



compText = """package packname;

import com.danceEngine.ecs.EComponent;

public class compname extends EComponent {

}
"""

sysText = """package packname;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;

public class sysname extends ESystem {
	

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(components)) {
componentDecl
		}
	}

}
"""
package = path[path.rfind("\\") + 1:]

compName = name
if(createComp):
    autoCompText = re.sub("packname", package, compText)
    autoCompText = re.sub("compname", compName, autoCompText)

if(createComp):
    name += "System"
autoSysTest = re.sub("sysname", name, sysText)
autoSysTest = re.sub("packname", package, autoSysTest)
compString = ""
compDecl = ""

if(createComp):
    components.insert(0, compName)

for i in range(len(components)):
    if(i != 0):
        compString += ", "
    compClass = components[i] + ".class"
    compString += compClass
    kürzel = ""
    for letter in re.findall("[A-Z]", components[i]):
        kürzel += letter
    kürzel = kürzel.lower()
    compDecl += "\t\t\t" + components[i] + " " + kürzel + " = e.getComponentByType(" + compClass + ");\n"
autoSysTest = re.sub("components", compString, autoSysTest)
autoSysTest = re.sub("componentDecl", compDecl, autoSysTest)

with open(path + "\\" + name + ".java", "x") as file:
    file.write(autoSysTest)
if(createComp):
    with open(path + "\\" + compName + ".java", "x") as file2:
        file2.write(autoCompText)

