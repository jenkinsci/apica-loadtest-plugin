/*
 * The MIT License
 *
 * Copyright 2015 andras.nemes.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.apica.apicaloadtest.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andras.nemes
 */
public class ThresholdDirectionFactory
{
    public static List<ThresholdDirection> getThresholdDirections()
    {
        List<ThresholdDirection> dirs = new ArrayList<>();
        dirs.add(new GreaterThan());
        dirs.add(new LessThan());
        return dirs;
    }
    
    public static ThresholdDirection getThresholdDirection(String description)
    {
        List<ThresholdDirection> dirs = getThresholdDirections();
        for (ThresholdDirection dir : dirs)
        {
            if (dir.isMatch(description))
            {
                return dir;
            }
        }
        
        return dirs.get(0);
    }
}
