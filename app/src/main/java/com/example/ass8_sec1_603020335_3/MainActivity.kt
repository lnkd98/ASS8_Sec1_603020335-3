    package com.example.ass8_sec1_603020335_3


    import android.content.DialogInterface
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.view.Menu
    import android.view.MenuItem
    import android.view.View
    import android.widget.AdapterView
    import android.widget.Toast
    import androidx.recyclerview.widget.DefaultItemAnimator
    import androidx.recyclerview.widget.DividerItemDecoration
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import kotlinx.android.synthetic.main.activity_main.*
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import java.text.FieldPosition

    class MainActivity : AppCompatActivity() {
        var employeeList = arrayListOf<Employee>()
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            recycler_view.layoutManager = LinearLayoutManager(applicationContext) as RecyclerView.LayoutManager?
            recycler_view.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
            recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL))

            recycler_view.addOnItemTouchListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {
                    Toast.makeText(applicationContext, "You click on : "+employeeList[position].emp_name,
                        Toast.LENGTH_SHORT).show()
                }

            })
        }

        override fun onResume() {
            super.onResume()
            callEmployeeData()
        }


        fun onClickEmployee(v: View) {
            val intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)

        }

        fun callEmployeeData(){

            employeeList.clear();

            val serv : EmployeeAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmployeeAPI ::class.java)

            serv.retrieveEmployee()
                .enqueue(object : Callback<List<Employee>> {

                    override fun onResponse(
                        call: Call<List<Employee>>,
                        response: Response<List<Employee>>
                    ) {
                        response.body()?.forEach{
                            employeeList.add(Employee(it.emp_name, it.emp_gender, it.emp_mail, it.emp_salary))
                        }

                        recycler_view.adapter = EmployeesAdapter(employeeList,applicationContext)
                        text1.text = "Employee List : "+ employeeList.size.toString()+ " Employees"
                    }

                    override fun onFailure(call: Call<List<Employee>>, t: Throwable) = t.printStackTrace()

                })
        }

        fun cancelEmp(view: View) {}
        fun addEmp(view: View) {}
    }
    interface  OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }
    fun RecyclerView.addOnItemTouchListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {

            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }