import matplotlib.pyplot as plt
import numpy as np
from scipy.interpolate import spline

def plot_smooth(xs, ys, label):
    xnew = np.linspace(xs.min(),xs.max(),300)
    y_smooth = spline(xs,ys,xnew)
    plt.plot(xnew, y_smooth, label=label)

def plot_points(xs, ys, label):
    plt.plot(xs, ys, label=label)





def plot_misclassified_5_20():
	xs = [0.05,0.10,0.15,0.20,0.25,0.30]
	xs = np.array(xs)
	misclassified_rate_normal = [0.022, 0.098, 0.319999, 0.346, 0.568, 0.62]
	misclassified_rate_sampling_10 = [0.32, 0.73, 0.758, 0.756, 0.758, 0.758]
	misclassified_rate_sampling_20 = [0.124, 0.308, 0.592, 0.632, 0.672, 0.686]
	misclassified_rate_sampling_30 = [0.024, 0.166, 0.274, 0.554, 0.55, 0.62]

	plot_smooth(xs, misclassified_rate_normal, 'misclassfied rate original')
	plot_smooth(xs, misclassified_rate_sampling_10, 'misclassfied rate sampling s=10')
	plot_smooth(xs, misclassified_rate_sampling_20, 'misclassfied rate sampling s=20')
	plot_smooth(xs, misclassified_rate_sampling_30, 'misclassfied rate sampling s=30')

	plt.legend(loc='best')
	plt.show()

def plot_misclassified_10_20():
	xs = [0.05,0.10,0.15, 0.20, 0.25,0.30]
	xs = np.array(xs)
	misclassified_rate_sampling_30 = [0.26799999999999996, 0.85, 0.853, 0.853, 0.853,0.853]
	plot_smooth(xs, misclassified_rate_sampling_30, 'misclassfied rate sampling s=30')

	plt.legend(loc='best')
	plt.show()


def plot_time():
	xs = np.array([20*i for i in range(1,9)])

	runtime_ms_normal = [2, 682, 7197, 35454, 112998, 301563, 729277, 1512259]
	runtime_ms_sample = [3, 582, 1627, 4954, 9746, 17939, 37941, 48329]

	plot_smooth(xs, runtime_ms_normal, 'time(ms) original')
	plot_smooth(xs, runtime_ms_sample, 'time(ms) sampling s=30')

	plt.legend(loc='best')
	plt.show()

# plot_misclassified_5_20()
plot_time()
