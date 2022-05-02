package com.example.brightplate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brightplate.main.ChosenRecipe
import com.example.brightplate.main.RecyclerAdapter
import com.example.brightplate.models.Recipe
import com.google.firebase.database.*
import kotlin.properties.Delegates

class RecipeListActivity : AppCompatActivity(), RecyclerAdapter.OnRecipeItemClickListener{

    private lateinit var dbref: DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipeRecyclerView = findViewById(R.id.recyclerView_recipeSelection)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        getRecipeData()

    }

    fun getRecipeList(): ArrayList<Recipe>
    {
        return recipeArrayList
    }

    private fun getRecipeData()
    {
        dbref = FirebaseDatabase.getInstance().getReference("Recipes")
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for(recipeSnapshot in snapshot.children)
                    {
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        recipeArrayList.add(recipe!!)
                    }

                }
                recipeRecyclerView.adapter = RecyclerAdapter(recipeArrayList, this@RecipeListActivity)

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }



    override fun onClick(position: Int) {
        Toast.makeText(this, "${recipeArrayList[position].getRecipeName()}: Position $position clicked!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@RecipeListActivity, ChosenRecipe:: class.java)
        startActivity(intent)
    }


}