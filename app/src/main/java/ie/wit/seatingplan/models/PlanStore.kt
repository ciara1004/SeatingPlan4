package ie.wit.seatingplan.models

interface PlanStore {
    fun findAll() : List<PlanModel>
    fun findById(id: Long) : PlanModel?
    fun create(plan: PlanModel)
    fun delete(plan:PlanModel)
}