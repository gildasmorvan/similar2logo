A Logo simulation multiple Langton’s ants. The model is inspired by this paper: 
http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf

When conflicting influences (removing or dropping a mark to the same location) are detected,  a specific policy is applied:
- if the parameter dropMark is true, the dropping influence takes precedent over the removing one and reciprocally.
- if the parameter removeDirectionChange is true, direction changes are not taken into account.

Two instances of the multi-turmite model are provided.
- fr.lgi2a.similar2logo.examples.multiturmite.TwoTurmitesSimulationMain defines a simple instance of the multi-turmite model with two turtles located at (x,y) and (x,y+1) and heading north. This simulation results in a growing squares. There are no collision in this simulation. Thus, parameters inverseMarkUpdate and removeDirectionChange have no effect.
- fr.lgi2a.similar2logo.examples.multiturmite.FourTurmitesSimulationMain defines an instance of the multi-turmite model with four turtles. This simulation results different cyclic or environment-filling behaviors according to the values of parameters inverseMarkUpdate and removeDirectionChange.