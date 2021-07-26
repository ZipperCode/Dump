package com.zipper.auto.api

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zipper.auto.api.adapter.DetailAdapter
import com.zipper.core.activity.BaseActivity

class ViewPointActivity : BaseActivity() {
    private lateinit var viewModel: ViewPointViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_point)
        totalCount = findViewById(R.id.total_count)
        viewModel = ViewModelProvider(this).get(ViewPointViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DetailAdapter(this, viewModel.getViewPoint()) {
            startActivity(
                Intent(
                    this,
                    DetailActivity::class.java
                ).apply { putExtra(DetailActivity.ID_KEY, it) })
        }
        viewModel.getViewPoint().observe(this, Observer{
            totalCount.text = "${it.size}"
        })

        val s = """FY2NF65rSB77HgPVgBaPMmeKNUi8URZQUT6\/E7PHd5BsIVUiFsZoZlH9Q0yWq+Kznkz3\/xdP23n5ZYg0GsxR0tTToDQJ9Gi6+Jg+AyOnfqnzoNiJjf0kninjL9zdbXtrK6mDa9\/9awHLC\/KWxmN830SLOsvD7SpvhFr0qg88wVasVQuXkJ0YZKrHiYX5Wpuk2ZdFiUhQnNE18YROWoZ\/JMkFcxQwnxPKPnamq\/zUBKvw6Faec+Btlo2h1mFhqXDrp+xwz9JS6ZeZLic7\/WjfmEEmBNdiNuDixGArVdIW16lyejOgVtqAYROH1J39qoGZgvMA6x33Dl7Ak+6JUcEglP7F1bjcRsmwFJvyKY8f5\/oZRDKlj77aGySpABHEnDEqiBRkhO7ckl\/2FUg+W7RpfS+qPvX+WWt5RrFiGA7ekf12IetsHdFKKKGs+I5GSai6xlldsQpBm9AvrpVUnDP2t16Pdypzal0rlU97sG3LGM4CCWTOkALZ62TdP0zvxoybR3j3q8\/RDmjCeR1NErjhJxEUd1XbMVAmh+GT0y+AecdXxH60f3ESMMTHLiCP2QeLP69sjwzDuNzgohkaudamCrnC+g5QFkIGFkl5LrBlxjzwViNjkdH4U\/Q3FzjmLCMjER0aXilrkE+9nSr8KMjHEZlNoCAuuMhFKeYRyHV0G0kKjxiCfoM+iYViGUN2r6IiSVwQLBuPv\/g0jBOB4U\/3i\/lOSVyy6n15KS7B8AjeuFrbSW4g5ep1yB7L++spw4urcPiSVrEd2qqmtmeHyhiRNxdFdCs7jqQUNiiIs+47GPoo0qcOysty5j4tS+Cvhh06BwA1\/G7K7vgJbyzgeU8PCqRtoNHj4p11D3dKlPY04\/YuDXkTo0ClT3uSeNpQ\/vnIKX803rVWF9c8NWGw+l3D2Rf5GeGg9IcQC1KJB1sPqADetHP1U8VvdOtbS8nNJSXKMDPrv0+6q\/m\/nCmW\/C2m\/VPeViFSkdhJ7\/fikQnzLDSTeQj\/+fjdgBaVM9V2UqtzHkWxsBZetz6T6nOe3dDrJV8dgDq2eiQ+Cvfry5w5us6sEpFjiTX+NDSmbwVJxrdeSrpPPvitvizbtKVXNLS4+QcYZJ\/cmJqGPvBfPVYgBlEPKrOTB4idgTHZKCb2CVl5SrpPPvitvizbtKVXNLS4+V8dgDq2eiQ+Cvfry5w5us7zJt9qNfb0jTtw6ppNRV1TNFu6TxjgpQCm\/RhtVkTJRQcYZJ\/cmJqGPvBfPVYgBlGjB4g57hbA+Q2D4AOredppvGIF7jMhLcBzhwVD\/dABVlfBRCDuoE5+OswjWl5a00fZuUvDTWcAzJCD9S8blhMsk3CRAvfJDqUn031TXDQrcaaXNCJVIrBrZX30ypEs52Ejq37ofYVWlpOMDTr4IQEUk3CRAvfJDqUn031TXDQrcZdtSmJwqKNtLhBTq6Jo9LfduNUzEEGjX3e3kRYg1PDqbM8cQ+WXqxOnhdcKQYQMMbEXm1QJwZNix4UCmLJiha01e9HiHcWL6ugS\/7Lho8eybM8cQ+WXqxOnhdcKQYQMMYd+vf3h9bZZ\/E7YSk0HBv3XoG2NQ1EOWCduQrFM3bUiVzZT++3Eqh7\/BXTHI7IR0TIR\/qGzqj4dvF2oSzjLFBFe1CwmEe9pdpbEIm8c1q+jx2eVJCqWb1BbVVzw1A\/8rOnQ3fTQtRth855fiK7ohtsTb6Wsc8bZ12JxygHnR5x5wrhNAXlYQeJc\/ICT\/fhyIcU1B7LCuowBGVj6m1YcSjxW2\/Ghe4oXgMk32uUEj4oM3h2t9HzK+C0AAVrj1rF4hJGnEthHwSg6MLWzfm6IXWoTTHxI4JNzHdSPoF9zhE8b0l64YdVN8PNvh+0tfc3aCBU+5ZGFSmdS60plUM5R5MpHGApuWBsrAMy5vTGTpqynaNOzFwcHMsSC1MyizvRvVWWe4emDy4E1eQcoR2DwFeb1ZbAQCMB4eyScQeQnzmM4C5dQG+U1Up03HZI0PXsEsTVcJcWF+sX4Q8EXPvDki7zph0Y13DYhzEfFuwz5h5HxwyxKA8mino2CmA3aDxDVhJRhQTumgjGLPQD6Lk9IKZM="""
        System.err.println("aes = ${AESUtils.decode(s)}")
    }
}