package com.example.a01appmovileskm

class ICities (
    public var name: String?,
    public var state: String?,
    public var country: String?,
    public var capital: Boolean?,
    public var population: Long?,
    public var regrions: List<String>?

){

    override fun toString(): String {

        return "${name} - ${country}"
    }

}


