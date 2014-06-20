A Logo simulation multiple Langton’s ants. The model is taken from this paper: 
http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf

When conflicting influences (removing or dropping a mark to the same location) are detected,  a specific policy is applied:
- if the parameter dropMark is true, the dropping influence takes precedent over the removing one and reciprocally.
- if the parameter inverseDirectionChange is true, direction changes are inverted. 