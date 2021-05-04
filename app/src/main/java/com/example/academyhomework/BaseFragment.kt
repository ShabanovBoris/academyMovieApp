package com.example.academyhomework

import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment()
{
      val parentRouter: Router? get() = (activity as? Router)

}
