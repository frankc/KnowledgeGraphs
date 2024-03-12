
from pyoxigraph import *
from query import *

# Read the turtle file
res = parse( "LLM.facts.ttl", mime_type="application/turtle", base_iri="http://codesupreme.com/" )

# Create RDF Store
store = Store( path="./store" )
for triple in res:
    store.add( Quad( triple.subject, triple.predicate, triple.object, DefaultGraph() ) )

# Demo queries
print( "\nLLMs:" )
for entity in is_a( store, "cs:LLM" ): print( entity )

print( "\nProperties of cs:DSPy:" )
for prop in properties( store, "cs:DSPy" ): print( prop )
