package tkhub.project.sfa.data.model.route

data class RouteBase(
    var error: Boolean,
    var message: String,
    var code:Int,
    var data : ArrayList<Route>
){
    constructor() : this(true, "",
        0, ArrayList<Route>()
    )

}