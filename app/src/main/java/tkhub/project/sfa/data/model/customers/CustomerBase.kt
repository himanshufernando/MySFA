package tkhub.project.sfa.data.model.customers

data class CustomerBase(
    var status: Boolean,
    var message: String,
    var code:Int,
    var data : ArrayList<Customer>
){
    constructor() : this(false, "",
        0, ArrayList<Customer>()
    )

}