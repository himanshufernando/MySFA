package tkhub.project.sfa.data.model.customers

data class CustomerStatusBase(
    var error: Boolean,
    var message: String,
    var code:Int,
    var data : ArrayList<CustomerStatus>
){
    constructor() : this(true, "",
        0, ArrayList<CustomerStatus>()
    )

}