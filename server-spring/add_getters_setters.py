import os, re

def add_methods(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    if '@Data' not in content:
        return

    content = content.replace('import lombok.Data;\n', '')
    content = content.replace('@Data\n', '')

    fields = re.findall(r'private\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+)(?:\s*=\s*[^;]+)?;', content)

    methods = []
    for ftype, fname in fields:
        capitalized = fname[0].upper() + fname[1:]
        getter = f"    public {ftype} get{capitalized}() {{ return {fname}; }}"
        setter = f"    public void set{capitalized}({ftype} {fname}) {{ this.{fname} = {fname}; }}"
        methods.append(getter)
        methods.append(setter)

    # Insert before the last '}'
    last_brace_idx = content.rfind('}')
    new_content = content[:last_brace_idx] + '\n' + '\n'.join(methods) + '\n}\n'

    with open(filepath, 'w') as f:
        f.write(new_content)

directory = 'src/main/java/com/srms/models'
for filename in os.listdir(directory):
    if filename.endswith('.java'):
        add_methods(os.path.join(directory, filename))
