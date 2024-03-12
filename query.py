

import re

# Create prefix list - used to create more concise queries
with open( "LLM.facts.ttl", "r" ) as f: lines = f.readlines()
    
pfx = ""

for line in lines:
    if re.match( "^@prefix.*", line ) is not None:
        tok = line.split()
        prefix = tok[1] 
        iri = tok[2]
        pfx += f"PREFIX {prefix} {iri}\n"


def is_a( store, t ):
    '''
    Given a type t, return a list of subjects of that type.
    '''
    query = pfx + f"SELECT ?s WHERE {{ ?s a {t} }}"
    return [ soln['s'] for soln in store.query( query ) ]

def properties( store, entity ):
    '''
    GIven an entity, return a list of that entity's properties.
    '''
    query = pfx + f"SELECT ?p ?o WHERE {{ {entity} ?p ?o }}"
    return [ (soln['p'], soln['o']) for soln in store.query( query ) ]
