# KS11
Incremental backup.
Still refining the same ol stoof.

I've just about gotten the core math-stuff where I think I want it, but it still needs some work.

I suppose I should say that the purpose of this is to reduce or eliminate the need for whatever specific nominal variant of Vector or Matrix etc you might use.
So like, no IntVector or FloatVector, no Vector2 or Vector3 etc, just aVector that takes on the type that you feed in.
aVector taking (1,1,1,1,1) results in aVector equivalent to an IntVect5. It's basically just aList of Number objects.

Matrices are similar, just ordered sets of these same Vectors. No need for mucking it up with needless differentiation lol.

Still collecting up my soft-rendering & other utils from the other repos
