package com.example.studentmanagementapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagementapplication.model.Student

class MainActivity : AppCompatActivity() {

    private lateinit var etMssv: EditText
    private lateinit var etName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView

    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    // save current student to edit
    private var currentEditingPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ View
        etMssv = findViewById(R.id.etMssv)
        etName = findViewById(R.id.etName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = StudentAdapter(studentList,
            onEditClick = { position ->
                showStudentInfo(position)
            },
            onDeleteClick = { position ->
                deleteStudent(position)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // handle add button
        btnAdd.setOnClickListener {
            val mssv = etMssv.text.toString()
            val name = etName.text.toString()

            if (mssv.isNotEmpty() && name.isNotEmpty()) {
                val newStudent = Student(name, mssv)
                studentList.add(newStudent)
                adapter.notifyItemInserted(studentList.size - 1)
                clearInput()
            } else {
                Toast.makeText(this, "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // handle update button
        btnUpdate.setOnClickListener {
            if (currentEditingPosition != -1) {
                val mssv = etMssv.text.toString()
                val name = etName.text.toString()

                if (mssv.isNotEmpty() && name.isNotEmpty()) {
                    // Cập nhật dữ liệu trong list
                    studentList[currentEditingPosition] = Student(name, mssv)
                    adapter.notifyItemChanged(currentEditingPosition)

                    // Reset trạng thái
                    currentEditingPosition = -1
                    clearInput()
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Hãy chọn sinh viên cần sửa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showStudentInfo(position: Int) {
        val student = studentList[position]
        etMssv.setText(student.mssv)
        etName.setText(student.name)

        currentEditingPosition = position
    }

    private fun deleteStudent(position: Int) {
        studentList.removeAt(position)
        adapter.notifyItemRemoved(position)

        if (currentEditingPosition == position) {
            currentEditingPosition = -1
            clearInput()
        }

        else if (currentEditingPosition > position) {
            currentEditingPosition--
        }

        adapter.notifyItemRangeChanged(position, studentList.size)
    }

    private fun clearInput() {
        etMssv.text.clear()
        etName.text.clear()
        etMssv.requestFocus()
    }
}