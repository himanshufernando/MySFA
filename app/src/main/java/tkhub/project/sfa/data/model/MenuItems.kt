package tkhub.project.sfa.data.model

data class MenuItems(
    var menuID: Long,
    var menuItemName: String,
    var menuItemID: Long,
    var menuItemCode: String,
    var menuCompanyID: Long,
    var isSelect : Boolean,

) {

    constructor() : this(0, "",
        0,"",0,false
    )
}